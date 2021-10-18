import { useState } from "react";
import { formatHyphen } from "../utils/format/phoneNumber";

const MAX_PHONE_NUMBER_LENGTH = 13;
const MAX_LENGTH_PHONE_NUMBER_HYPHEN_IDX = [3, 7];
const PHONE_NUMBER_HYPHEN_IDX = [2, 6];

const useApplicantRegisterForm = () => {
  const [phoneNumber, setPhoneNumber] = useState("");

  const handlePhoneNumberChange = ({ nativeEvent: { data }, target: { value } }) => {
    if (Number.isNaN(data) || value.length > MAX_PHONE_NUMBER_LENGTH) return;

    const [firstHyphenIdx, secondHyphenIdx] =
      value.length !== MAX_PHONE_NUMBER_LENGTH
        ? PHONE_NUMBER_HYPHEN_IDX
        : MAX_LENGTH_PHONE_NUMBER_HYPHEN_IDX;

    const result = formatHyphen(value, firstHyphenIdx, secondHyphenIdx).trim();

    setPhoneNumber(result);
  };

  return { phoneNumber, handlePhoneNumberChange };
};

export default useApplicantRegisterForm;
