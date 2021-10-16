import { useState } from "react";
import { formatHyphen } from "../utils/format/phoneNumber";

const MAX_PHONE_NUMBER_LENGTH = 13;
const FIRST_HYPHEN_IDX = 3;
const SECOND_HYPHEN_IDX = 7;

const usePhoneNumber = () => {
  const [phoneNumber, setPhoneNumber] = useState("");

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

    setPhoneNumber(result);
  };

  return { phoneNumber, handlePhoneNumberChange, setPhoneNumber };
};

export default usePhoneNumber;
