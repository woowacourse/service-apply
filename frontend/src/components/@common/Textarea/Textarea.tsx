import classNames from "classnames";
import styles from "./Textarea.module.css";

export type TextareaProps = React.TextareaHTMLAttributes<HTMLTextAreaElement> & {
  maxLength?: number;
  value?: string;
  className?: string;
  readOnly?: boolean;
  onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
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
