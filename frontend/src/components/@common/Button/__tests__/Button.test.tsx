import { render, screen } from "@testing-library/react";
import Button from "../Button";

describe("Button 컴포넌트", () => {
  test("Button을 렌더할 수 있어야 한다.", () => {
    render(<Button />);

    expect(screen.getByRole("button")).toBeInTheDocument();
  });

  test("버튼 텍스트와 함께 Button을 렌더할 수 있어야 한다.", () => {
    const label = "button label";

    render(<Button>{label}</Button>);

    expect(screen.getByText(label)).toBeInTheDocument();
  });
});
