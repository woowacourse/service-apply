import { useState } from "react";
import { formatHyphen } from "../utils/format/phoneNumber";

const MAX_PHONE_NUMBER_LENGTH = 13;

const useApplicantRegisterForm = () => {
  const [phoneNumber, setPhoneNumber] = useState("");

  const handlePhoneNumberChange = ({
    nativeEvent: { data },
    target: { value },
  }) => {
    if (Number.isNaN(data) || value.length > MAX_PHONE_NUMBER_LENGTH) return;

    const firstHyphenIdx = value.length === MAX_PHONE_NUMBER_LENGTH ? 3 : 2;
    const secondHyphenIdx = value.length === MAX_PHONE_NUMBER_LENGTH ? 7 : 6;

    const result = formatHyphen(value, firstHyphenIdx, secondHyphenIdx).trim();

    setPhoneNumber(result);
  };

  return { phoneNumber, handlePhoneNumberChange };
};

export default useApplicantRegisterForm;
