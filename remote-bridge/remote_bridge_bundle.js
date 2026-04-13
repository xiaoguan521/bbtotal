(() => {
  const globalTarget = typeof window !== 'undefined' ? window : globalThis;
  const namespace = globalTarget.__bbtotalRemoteBridgeBundle || {};

  globalTarget.__bbtotalRemoteBridgeBundle = {
    ...namespace,
    version: '2026.04.13.1',
    source: 'github-pages',
    loadedAt: Date.now(),
    notes: [
      'Put frequently changing webview compatibility patches here.',
      'This file is loaded after the built-in bridge and can override page-side behavior.',
    ],
  };
})();
