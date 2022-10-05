import classNames from "classnames";
import styles from "./TextInput.module.css";

export type TextInputProps = React.InputHTMLAttributes<HTMLInputElement> & {
  type?: "text" | "email" | "password" | "tel" | "number" | "url";
  value?: string;
};

const TextInput = ({
  maxLength,
  type = "text",
  value = "",
  className = "",
  readOnly = false,
  onChange,
  ...props
}: TextInputProps) => {
  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (
      onChange === undefined ||
      (maxLength !== undefined && event.target.value.length > maxLength)
    ) {
      return;
    }

    onChange(event);
  };

  const handleWhiteSpace = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === " ") {
      event.preventDefault();
    }
  };

  return (
    <input
      type={type}
      value={value}
      className={classNames(styles["text-input"], className)}
      readOnly={readOnly}
      onKeyDown={handleWhiteSpace}
      onChange={handleChange}
      {...props}
    />
  );
};

export default TextInput;
