import classNames from "classnames";
import styles from "./Textarea.module.css";

const MAX_LENGTH_VALIDATION_MESSAGE = (maxLength: number) =>
  `최대 ${maxLength}자까지 입력 가능합니다.`;

export type TextareaProps = React.TextareaHTMLAttributes<HTMLTextAreaElement> & {
  value?: string;
  onChange: React.ChangeEventHandler<HTMLTextAreaElement>;
};

const Textarea = ({
  maxLength,
  value = "",
  className = "",
  readOnly = false,
  onChange,
  ...props
}: TextareaProps) => {
  const handleChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    event.target.value = event.target.value.replaceAll("\r\n", "\n");

    if (maxLength !== undefined && event.target.value.length > maxLength) {
      event.target.value = event.target.value.slice(0, maxLength);
      alert(MAX_LENGTH_VALIDATION_MESSAGE(maxLength));
    }

    onChange(event);
  };

  const trimEnd = (event: React.FocusEvent<HTMLTextAreaElement>) => {
    event.target.value = event.target.value.trimEnd();

    onChange(event);
  };

  return (
    <textarea
      value={value}
      onChange={handleChange}
      className={classNames(styles["text-input"], className)}
      readOnly={readOnly}
      onBlur={trimEnd}
      {...props}
    />
  );
};

export default Textarea;
