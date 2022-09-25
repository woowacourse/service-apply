import React, { TextareaHTMLAttributes } from "react";
import classNames from "classnames";
import Description from "../Description/Description";
import Label from "../Label/Label";
import Textarea from "../Textarea/Textarea";
import styles from "./MessageTextarea.module.css";

export type MessageTextareaProps = TextareaHTMLAttributes<HTMLTextAreaElement> & {
  className?: string;
  label?: string;
  required?: boolean;
  description?: string;
  maxLength?: number;
  value?: string;
  name: string;
  errorMessage?: string;
  onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
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
  onChange,
  ...props
}: MessageTextareaProps) => {
  return (
    <div className={classNames(styles.box, className)}>
      <div className={styles["text-field"]}>
        <Label className={styles.label} required={required}>
          {label}
        </Label>
        {description && <Description>{description}</Description>}
        {maxLength && maxLength > 0 && (
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
