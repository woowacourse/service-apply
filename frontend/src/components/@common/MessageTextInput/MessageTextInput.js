import classNames from "classnames";
import PropTypes from "prop-types";
import Description from "../Description/Description";
import Label from "../Label/Label";
import TextInput from "../TextInput/TextInput";
import styles from "./MessageTextInput.module.css";

const MessageTextInput = ({
  label,
  description,
  initialValue,
  value,
  onChange,
  name,
  maxLength,
  required,
  className,
  errorMessage,
  rightButton,
  ...props
}) => {
  return (
    <div className={classNames(styles.box, className)}>
      <div className={styles["text-field"]}>
        <Label className={styles.label} required={required}>
          {label}
        </Label>
        {description && (
          <Description className={styles.description}>
            {description}
          </Description>
        )}
        <div class={styles["input-box"]}>
          <TextInput
            required={required}
            value={value}
            name={name}
            maxLength={maxLength}
            onChange={onChange}
            {...props}
          />
          {rightButton}
        </div>
      </div>
      {errorMessage && <p className={styles["rule-field"]}>{errorMessage}</p>}
    </div>
  );
};

MessageTextInput.propTypes = {
  label: PropTypes.string,
  initialValue: PropTypes.string,
  name: PropTypes.string.isRequired,
  required: PropTypes.bool,
  description: PropTypes.node,
  maxLength: PropTypes.number,
  errorMessage: PropTypes.string,
  rightButton: PropTypes.node,
};

MessageTextInput.defaultProps = {
  label: "",
  initialValue: "",
  required: false,
  description: "",
};

export default MessageTextInput;
