import { useState } from "react";
import useInterval from "./useInterval";

const useTimer = (initialSeconds) => {
  const [seconds, setSeconds] = useState(initialSeconds);
  const [delay, setDelay] = useState(null);

  useInterval(() => {
    setSeconds(seconds - 1);
  }, delay);

  const handleStart = () => setDelay(1000);

  const handleStop = () => setDelay(null);

  return {
    timerSeconds: seconds,
    setTimerSeconds: setSeconds,
    handleStartTimer: handleStart,
    handleStopTimer: handleStop,
  };
};

export default useTimer;
