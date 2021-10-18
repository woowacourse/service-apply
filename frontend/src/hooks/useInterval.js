import { useEffect, useRef } from 'react';

const useInterval = (callback, delay) => {
  const savedCallback = useRef();

  useEffect(() => {
    savedCallback.current = callback;
  }, [callback]);

  useEffect(() => {
    const tick = () => {
      savedCallback.current();
    };

    if (typeof delay !== 'number' || delay < 0) return;

    const id = setInterval(tick, delay);

    return () => clearInterval(id);
  }, [delay]);
};

export default useInterval;
