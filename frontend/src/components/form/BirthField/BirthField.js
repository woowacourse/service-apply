import React from 'react';
import PropTypes from 'prop-types';
import Field from '../Field/Field';
import Label from '../Label/Label';
import TextInput from '../TextInput/TextInput';
import './BirthField.css';

const BirthField = ({ required }) => {
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
          placeholder="YYYY"
          required={required}
        />
        <TextInput
          className="month"
          name="month"
          type="text"
          placeholder="MM"
          required={required}
        />
        <TextInput
          className="day"
          name="day"
          type="text"
          placeholder="DD"
          required={required}
        />
      </div>
    </Field>
  );
};

BirthField.propTypes = {
  required: PropTypes.bool,
};

export default BirthField;
