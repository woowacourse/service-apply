import { useState } from "react";
import { ERROR_MESSAGE } from "../constants/messages";
import { formatHyphen } from "../utils/format/phoneNumber";
import { isValidPhoneNumber } from "../utils/validation/phoneNumber";

const MAX_PHONE_NUMBER_LENGTH = 13;
const FIRST_HYPHEN_IDX = 3;
const SECOND_HYPHEN_IDX = 7;

const usePhoneNumber = () => {
  const [phoneNumber, setPhoneNumber] = useState("");
  const [phoneNumberErrorMessage, setErrorMessage] = useState("");

  const handlePhoneNumberChange = ({
    nativeEvent: { data },
    target: { value },
  }) => {
    if (isNaN(data) || value.length > MAX_PHONE_NUMBER_LENGTH) return;

    const result = formatHyphen(
      value,
      FIRST_HYPHEN_IDX,
      SECOND_HYPHEN_IDX
    ).trim();

    value && !isValidPhoneNumber(value)
      ? setErrorMessage(ERROR_MESSAGE.VALIDATION.PHONE_NUMBER)
      : setErrorMessage("");

    setPhoneNumber(result);
  };

  return {
    phoneNumber,
    handlePhoneNumberChange,
    setPhoneNumber,
    phoneNumberErrorMessage,
  };
};

export default usePhoneNumber;
