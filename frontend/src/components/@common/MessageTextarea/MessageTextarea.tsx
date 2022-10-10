import classNames from "classnames";
import Description from "../Description/Description";
import Label from "../Label/Label";
import Textarea from "../Textarea/Textarea";
import styles from "./MessageTextarea.module.css";

export type MessageTextareaProps = React.TextareaHTMLAttributes<HTMLTextAreaElement> & {
  label?: string;
  description?: string;
  value?: string;
  name: string;
  errorMessage?: string;
  showCount?: boolean;
  onChange: React.ChangeEventHandler<HTMLTextAreaElement>;
};

const MessageTextarea = ({
  className,
  label = "",
  required = false,
  description = "",
  maxLength,
  value = "",
  name,
  errorMessage,
  showCount = false,
  onChange,
  ...props
}: MessageTextareaProps) => {
  const shouldShowCount = showCount && maxLength && maxLength > 0;

  return (
    <div className={classNames(styles.box, className)}>
      <div className={styles["text-field"]}>
        <Label className={styles.label} required={required}>
          {label}
        </Label>
        {description && <Description>{description}</Description>}
        {shouldShowCount && (
          <div className={styles["length-limit"]}>
            {value?.length} / {maxLength}
          </div>
        )}
        <Textarea
          name={name}
          required={required}
          value={value}
          onChange={onChange}
          maxLength={maxLength}
          {...props}
        />
      </div>
      <p className={styles["rule-field"]}>{errorMessage}</p>
    </div>
  );
};

export default MessageTextarea;
