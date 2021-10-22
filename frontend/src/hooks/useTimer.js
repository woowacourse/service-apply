import { useState } from "react";
import useInterval from "./useInterval";

const convertDateToSeconds = (dateMilliseconds) => {
  return Math.floor(dateMilliseconds / 1000);
};

const useTimer = (initialSeconds = 0, initialDelay = 1000) => {
  const [timerEndSeconds, setTimerEndSeconds] = useState(null);
  const [remainingSeconds, setRemainingSeconds] = useState(initialSeconds);
  const [delay, setDelay] = useState(initialDelay);

  useInterval(() => {
    if (!timerEndSeconds) return;

    const now = convertDateToSeconds(Date.now());

    setRemainingSeconds(timerEndSeconds - now);
  }, delay);

  const start = () => {
    const nowDate = new Date();
    const endMilliSeconds = new Date().setSeconds(nowDate.getSeconds() + initialSeconds);

    setTimerEndSeconds(convertDateToSeconds(endMilliSeconds));
    setRemainingSeconds(initialSeconds);
    setDelay(initialDelay);
  };

  const stop = () => setDelay(null);

  const reset = () => {
    stop();
    setRemainingSeconds(initialSeconds);
  };

  return {
    timerSeconds: remainingSeconds,
    setTimerSeconds: setRemainingSeconds,
    startTimer: start,
    stopTimer: stop,
    resetTimer: reset,
  };
};

export default useTimer;
