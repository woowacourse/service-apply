import { useState } from "react";
import useInterval from "./useInterval";

const useTimer = (initialSeconds, initialDelay = 1000) => {
  const [seconds, setSeconds] = useState(initialSeconds);
  const [delay, setDelay] = useState(initialDelay);

  useInterval(() => {
    setSeconds(seconds - 1);
  }, delay);

  const start = () => setDelay(initialDelay);

  const stop = () => setDelay(null);

  return {
    timerSeconds: seconds,
    setTimerSeconds: setSeconds,
    startTimer: start,
    stopTimer: stop,
  };
};

export default useTimer;
