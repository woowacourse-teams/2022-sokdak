const throttle = (function () {
  let timerID: NodeJS.Timer | null = null;
  return function (callback: () => void, delay: number) {
    return function () {
      if (timerID) {
        return;
      }
      timerID = setTimeout(() => {
        timerID = null;
        callback();
      }, delay);
    };
  };
})();

export default throttle;
