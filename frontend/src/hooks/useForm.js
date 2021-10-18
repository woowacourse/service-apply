import { useState } from 'react';

import { ERROR_MESSAGE } from '../constants/messages';

const useForm = ({ validators, submit }) => {
  const [value, setValue] = useState({});
  const [isRequiredMap, setIsRequiredMap] = useState({});
  const [errorMessage, setErrorMessage] = useState({});

  const isValid = Object.values(errorMessage).filter(Boolean).length === 0;
  const isEmpty =
    Object.entries(value)
      .filter(([key]) => isRequiredMap[key])
      .filter(([_, val]) => val.length > 0 || val === true).length <
    Object.keys(isRequiredMap).length;

  const handleChange = ({ target }) => {
    setValue((prev) => ({
      ...prev,
      [target.name]: target.type === 'checkbox' ? target.checked : target.value,
    }));

    const validator = validators?.[target.name];

    if (!validator) return;

    try {
      const result = validator(target.value);

      if (typeof result === 'function') {
        result(value);
      }

      setErrorMessage((prev) => ({ ...prev, [target.name]: null }));
    } catch (error) {
      setErrorMessage((prev) => ({ ...prev, [target.name]: error.message }));
    }
  };

  const handleCapsLockState = (event) => {
    if (event.target.type !== 'password') return;

    setErrorMessage((prev) => ({
      ...prev,
      [event.target.name]: event.getModifierState('CapsLock')
        ? ERROR_MESSAGE.VALIDATION.PASSWORD_CAPSLOCK
        : prev[event.target.name],
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!isValid) return;

    submit(value);
  };

  const register = (name, initialValue = '', isRequired) => {
    setValue((prev) => ({ ...prev, [name]: initialValue }));
    setErrorMessage((prev) => ({ ...prev, [name]: null }));

    if (isRequired) {
      setIsRequiredMap((prev) => ({ ...prev, [name]: isRequired }));
    }
  };

  const unRegister = (name) => {
    const { [name]: deletedValue, ...newValue } = value;
    const { [name]: deletedErrormessage, ...newErrorMessage } = errorMessage;
    const { [name]: deletedisRequired, ...newisRequired } = isRequiredMap;

    setValue(newValue);
    setErrorMessage(newErrorMessage);
    setIsRequiredMap(newisRequired);
  };

  const reset = (name) => {
    setValue((prev) => ({
      ...prev,
      [name]: '',
    }));

    setErrorMessage((prev) => ({
      ...prev,
      [name]: null,
    }));
  };

  const resetAll = () => {
    setValue((prev) => {
      for (const name in prev) {
        if (typeof prev[name] === 'boolean') {
          prev[name] = false;
        }

        prev[name] = '';
      }

      return { ...prev };
    });

    setErrorMessage((prev) => {
      for (const name in prev) {
        prev[name] = null;
      }

      return { ...prev };
    });
  };

  return {
    value,
    errorMessage,
    isValid,
    isEmpty,
    handleChange,
    handleCapsLockState,
    handleSubmit,
    register,
    unRegister,
    reset,
    resetAll,
  };
};

export default useForm;
