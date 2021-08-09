import React from "react";
import PropTypes from "prop-types";
import Field from "../Field/Field";
import Label from "../Label/Label";
import TextInput from "../TextInput/TextInput";
import "./BirthField.css";

const BirthField = ({ required, value, onChange }) => {
  return (
    <Field className="birth-field">
      <Label for="year" required={required}>
        생년월일
      </Label>
      <div className="birth">
        <TextInput
          className="year"
          id="year"
          name="year"
          type="text"
          value={value.year}
          placeholder="YYYY"
          maxLength={4}
          required={required}
          onChange={onChange}
        />
        <TextInput
          className="month"
          name="month"
          type="text"
          value={value.month}
          placeholder="MM"
          maxLength={2}
          required={required}
          onChange={onChange}
        />
        <TextInput
          className="day"
          name="day"
          type="text"
          value={value.day}
          placeholder="DD"
          maxLength={2}
          required={required}
          onChange={onChange}
        />
      </div>
    </Field>
  );
};

BirthField.propTypes = {
  required: PropTypes.bool,
};

BirthField.defaultProps = {
  required: false,
};

export default BirthField;
