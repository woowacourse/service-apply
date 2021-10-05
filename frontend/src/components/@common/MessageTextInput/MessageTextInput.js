import PropTypes from "prop-types";
import Description from "../Description/Description";
import TextInput from "../TextInput/TextInput";
import styles from "./MessageTextInput.module.css";
import Label from "../Label/Label";

const MessageTextInput = ({
  label,
  description,
  initialValue,
  value,
  onChange,
  name,
  maxLength,
  required,
  errorMessage,
  ...props
}) => {
  return (
    <>
      <div className={styles["text-field"]}>
        <Label required={required}>{label}</Label>
        {description && <Description>{description}</Description>}
        <TextInput
          required={required}
          value={value}
          name={name}
          maxLength={maxLength}
          onChange={onChange}
          {...props}
        />
      </div>
      <p className={styles["rule-field"]}>{errorMessage}</p>
    </>
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
};

MessageTextInput.defaultProps = {
  label: "",
  initialValue: "",
  required: false,
  description: "",
};

export default MessageTextInput;
