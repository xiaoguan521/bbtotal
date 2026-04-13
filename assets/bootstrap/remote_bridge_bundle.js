(() => {
  const globalTarget = typeof window !== 'undefined' ? window : globalThis;
  if (globalTarget.__bbtotalRemoteBridgePatchInstalled) {
    return;
  }
  globalTarget.__bbtotalRemoteBridgePatchInstalled = true;

  const workflowKeys = [
    'bpmid',
    'businessKey',
    'processKey',
    'bpmparam',
    'taskid',
    'taskId',
    'flowtype',
    'newdaiban',
    'nodeType',
    'taskDefinitionKey',
    'processInstanceId',
    'processDefinitionId',
    'taskName',
  ];

  const parseObject = (value) => {
    if (!value) {
      return {};
    }
    if (typeof value === 'object' && !Array.isArray(value)) {
      return { ...value };
    }
    if (typeof value === 'string') {
      try {
        const parsed = JSON.parse(value);
        if (parsed && typeof parsed === 'object' && !Array.isArray(parsed)) {
          return { ...parsed };
        }
      } catch (_) {}
    }
    return {};
  };

  const safeStringify = (value) => {
    try {
      return JSON.stringify(value);
    } catch (_) {
      return '{}';
    }
  };

  const getRuntime = () => {
    return parseObject(globalTarget.__bbtotalRuntime);
  };

  const mergeWorkflowFields = (...sources) => {
    const merged = {};
    sources.forEach((source) => {
      const objectSource = parseObject(source);
      workflowKeys.forEach((key) => {
        const value = objectSource[key];
        if (value !== undefined && value !== null && String(value).trim()) {
          merged[key] = value;
        }
      });
    });
    if (merged.taskId && !merged.taskid) {
      merged.taskid = merged.taskId;
    }
    if (merged.taskid && !merged.taskId) {
      merged.taskId = merged.taskid;
    }
    return merged;
  };

  const applyStorage = (payload) => {
    const data = mergeWorkflowFields(payload);
    [globalTarget.localStorage, globalTarget.sessionStorage].forEach((store) => {
      if (!store) {
        return;
      }
      Object.entries(data).forEach(([key, value]) => {
        try {
          store.setItem(key, String(value));
        } catch (_) {}
      });
    });
  };

  const syncObjects = (workflowFields) => {
    const runtime = getRuntime();
    const currentUserinfo = parseObject(
      runtime.userInfo || runtime.userInfoJson || globalTarget.params,
    );
    const currentParams = parseObject(
      runtime.pageParams || runtime.pageParamsJson || globalTarget.params,
    );
    const currentLaunch = parseObject(
      runtime.launchPayload || runtime.launchPayloadJson || globalTarget.params2,
    );
    const currentBbgrxx = parseObject(globalTarget.bbgrxx || runtime.bbgrxx);

    const mergedLaunch = {
      ...currentLaunch,
      ...workflowFields,
    };
    const mergedUserinfo = {
      ...currentUserinfo,
      ...workflowFields,
    };
    const mergedParams = {
      ...currentParams,
      ...workflowFields,
    };
    const mergedBbgrxx = {
      ...currentBbgrxx,
      ...workflowFields,
    };

    globalTarget.__bbtotalLaunchPayload = mergedLaunch;
    globalTarget.__bbtotalLaunchPayloadJson = safeStringify(mergedLaunch);
    globalTarget.params2 = globalTarget.__bbtotalLaunchPayloadJson;

    globalTarget.__bbtotalPageParams = mergedParams;
    globalTarget.__bbtotalPageParamsJson = safeStringify(mergedParams);
    globalTarget.params = globalTarget.__bbtotalPageParamsJson;

    globalTarget.bbgrxx = mergedBbgrxx;

    if (typeof globalTarget.__bbtotalSyncBbgrxxFromUserinfo === 'function') {
      try {
        globalTarget.__bbtotalSyncBbgrxxFromUserinfo(mergedUserinfo);
      } catch (_) {}
    }

    if (typeof globalTarget.__bbtotalSyncBbgrxx === 'function') {
      try {
        globalTarget.__bbtotalSyncBbgrxx(mergedBbgrxx);
      } catch (_) {}
    }

    applyStorage(mergedLaunch);
    applyStorage(mergedUserinfo);

    return {
      params: mergedParams,
      params2: mergedLaunch,
      userinfo: mergedUserinfo,
      bbgrxx: mergedBbgrxx,
    };
  };

  const applyKnownWorkflowFixes = () => {
    const runtime = getRuntime();
    const workflowFields = mergeWorkflowFields(
      runtime.launchPayload,
      runtime.pageParams,
      runtime.userInfo,
      runtime.bbgrxx,
      globalTarget.__bbtotalLaunchPayload,
      globalTarget.params2,
      globalTarget.params,
      globalTarget.bbgrxx,
    );
    if (!Object.keys(workflowFields).length) {
      return null;
    }
    return syncObjects(workflowFields);
  };

  const patchAddDataSouce = () => {
    const current = globalTarget.addDataSouce || globalTarget.window?.addDataSouce;
    if (typeof current !== 'function' || current.__bbtotalWorkflowPatched) {
      return typeof current === 'function';
    }

    const wrapped = function patchedAddDataSouce(params, params2, ...rest) {
      const workflowFields = mergeWorkflowFields(params2, params);
      if (Object.keys(workflowFields).length) {
        const synced = syncObjects(workflowFields);
        return current.call(
          this,
          safeStringify({ ...parseObject(params), ...synced.params }),
          safeStringify({ ...parseObject(params2), ...synced.params2 }),
          ...rest,
        );
      }
      return current.call(this, params, params2, ...rest);
    };

    wrapped.__bbtotalWorkflowPatched = true;
    globalTarget.addDataSouce = wrapped;
    if (globalTarget.window) {
      globalTarget.window.addDataSouce = wrapped;
    }
    return true;
  };

  applyKnownWorkflowFixes();
  patchAddDataSouce();

  let attempts = 0;
  const timer = globalTarget.setInterval(() => {
    attempts += 1;
    applyKnownWorkflowFixes();
    if (patchAddDataSouce() || attempts >= 120) {
      if (attempts >= 120 || patchAddDataSouce()) {
        globalTarget.clearInterval(timer);
      }
    }
  }, 250);

  globalTarget.__bbtotalRemoteBridgeBundle = {
    version: '2026.04.13.2',
    source: 'embedded',
    loadedAt: Date.now(),
    fixes: [
      'sync workflow fields from params2 into params/userinfo/bbgrxx',
      'persist taskid and bpmparam into local/session storage',
      'wrap addDataSouce to prevent task context loss on page init',
    ],
    applyKnownWorkflowFixes,
  };
})();
