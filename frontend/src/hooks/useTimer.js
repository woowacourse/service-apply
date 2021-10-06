import { useState } from "react";
import useInterval from "./useInterval";

const useTimer = (initialSeconds) => {
  const [seconds, setSeconds] = useState(initialSeconds);
  const [delay, setDelay] = useState(null);

  useInterval(() => {
    setSeconds(seconds - 1);
  }, delay);

  const onStart = () => setDelay(1000);

  const onStop = () => setDelay(null);

  return { timerSeconds: seconds, setTimer: setSeconds, onStart, onStop };
};

export default useTimer;
