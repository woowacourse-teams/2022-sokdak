//@ts-nocheck
export default function runJenniferFront() {
  if (process.env.MODE == 'LOCAL:MSW') {
    (function (j, ennifer) {
      j['dmndata'] = [];
      j['jenniferFront'] = function (args) {
        window.dmndata.push(args);
      };
      j['dmnaid'] = ennifer;
      j['dmnatime'] = new Date();
      j['dmnanocookie'] = false;
      j['dmnajennifer'] = 'JENNIFER_FRONT@INTG';
    })(window, '4ed68da');
  }
}
