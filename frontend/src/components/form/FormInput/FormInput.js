import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import useFormContext from '../../../hooks/useFormContext';
import MessageTextInput from '../../@common/MessageTextInput/MessageTextInput';

const FormInput = ({ label, description, initialValue, name, maxLength, required, ...props }) => {
  const { value, errorMessage, handleChange, register, unRegister } = useFormContext();

  useEffect(() => {
    register(name, initialValue, required);

    return () => {
      unRegister(name);
    };
  }, [name, initialValue]);

  return (
    <MessageTextInput
      value={value[name]}
      onChange={handleChange}
      errorMessage={errorMessage[name]}
      label={label}
      description={description}
      initialValue={initialValue}
      name={name}
      maxLength={maxLength}
      required={required}
      {...props}
    />
  );
};

FormInput.propTypes = {
  label: PropTypes.string,
  initialValue: PropTypes.string,
  name: PropTypes.string.isRequired,
  required: PropTypes.bool,
  description: PropTypes.node,
  maxLength: PropTypes.number,
  errorMessage: PropTypes.string,
};

FormInput.defaultProps = {
  label: '',
  initialValue: '',
  required: false,
  description: '',
};

export default FormInput;
