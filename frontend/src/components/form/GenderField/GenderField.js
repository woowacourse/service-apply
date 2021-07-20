import React from 'react';
import Field from '../Field/Field';
import Label from '../Label/Label';
import Radio from '../Radio/Radio';
import './GenderField.css';

const GenderField = () => {
  return (
    <Field className="gender-field">
      <Label required>성별</Label>
      <div class="gender-group">
        <Radio name="gender" label="남자" value="male" required />
        <Radio name="gender" label="여자" value="female" required />
      </div>
    </Field>
  );
};

export default GenderField;
