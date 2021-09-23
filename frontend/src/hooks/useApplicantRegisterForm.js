import { useState } from "react";

const MAX_PHONE_NUMBER_LENGTH = 13;

const setHyphen = (str, firstHyphenIdx, secondHyphenIdx) =>
  str
    .replaceAll("-", "")
    .split("")
    .map((el, idx) =>
      idx === firstHyphenIdx || idx === secondHyphenIdx ? ["-", el] : el
    )
    .flat()
    .join("");

const useApplicantRegisterForm = () => {
  const [phoneNumber, setPhoneNumber] = useState("");

  const handlePhoneNumberChange = ({
    nativeEvent: { data },
    target: { value },
  }) => {
    if (Number.isNaN(data) || value.length > MAX_PHONE_NUMBER_LENGTH) return;

    const firstHyphenIdx = value.length === MAX_PHONE_NUMBER_LENGTH ? 3 : 2;
    const secondHyphenIdx = value.length === MAX_PHONE_NUMBER_LENGTH ? 7 : 6;

    const result = setHyphen(value, firstHyphenIdx, secondHyphenIdx).trim();

    setPhoneNumber(result);
  };

  return { phoneNumber, handlePhoneNumberChange };
};

export default useApplicantRegisterForm;
