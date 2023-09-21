import { renderHook } from "@testing-library/react";
import { describe, expect, test } from "@jest/globals";

import useInterval from "../useInterval";

describe("useInterval", () => {
  beforeAll(() => {
    jest.useFakeTimers();
  });

  afterAll(() => {
    jest.runOnlyPendingTimers();
    jest.useRealTimers();
  });

  test("처음 렌더될 때는 콜백을 호출하지 않아야 한다.", () => {
    const callback = jest.fn();
    const delay = 1000;

    const { unmount } = renderHook(() => useInterval(callback, delay));

    expect(callback).not.toHaveBeenCalled();
    unmount();
  });

  test("지정된 시간 간격으로 콜백을 호출해야 한다.", () => {
    const callback = jest.fn();
    const delay = 1000;
    const callCount = 3;

    const { unmount } = renderHook(() => useInterval(callback, delay));
    jest.advanceTimersByTime(delay * callCount);

    expect(callback).toHaveBeenCalledTimes(callCount);
    unmount();
  });

  test("훅을 사용하는 컴포넌트가 unmount된 이후에는 시간이 지나도 지정된 콜백이 호출되지 않아야 한다.", () => {
    const callback = jest.fn();
    const delay = 1000;

    const { unmount } = renderHook(() => useInterval(callback, delay));
    unmount();
    jest.advanceTimersByTime(delay);

    expect(callback).toHaveBeenCalledTimes(0);
  });
});
