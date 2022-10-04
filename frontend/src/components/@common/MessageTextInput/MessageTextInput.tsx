import classNames from "classnames";
import Description from "../Description/Description";
import Label from "../Label/Label";
import TextInput from "../TextInput/TextInput";
import styles from "./MessageTextInput.module.css";

export type MessageTextInputProps = Omit<React.InputHTMLAttributes<HTMLInputElement>, "type"> & {
  className?: string;
  label?: string;
  description?: string;
  value?: string;
  name: string;
  errorMessage?: string;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
};

const MessageTextInput = ({
  className,
  label = "",
  required = false,
  description = "",
  value = "",
  name,
  maxLength,
  errorMessage,
  onChange,
  ...props
}: MessageTextInputProps) => {
  return (
    <div className={classNames(styles.box, className)}>
      <div className={styles["text-field"]}>
        <Label className={styles.label} required={required}>
          {label}
        </Label>
        {description && <Description className={styles.description}>{description}</Description>}
        <div className={styles["input-box"]}>
          <TextInput
            required={required}
            value={value}
            name={name}
            maxLength={maxLength}
            onChange={onChange}
            {...props}
          />
        </div>
        {errorMessage && <p className={styles["rule-field"]}>{errorMessage}</p>}
      </div>
    </div>
  );
};

export default MessageTextInput;
