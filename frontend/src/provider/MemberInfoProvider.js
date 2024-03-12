import React, { useEffect, useState } from "react";

import * as Api from "../api";
import { ERROR_MESSAGE } from "../constants/messages";
import useTokenContext from "../hooks/useTokenContext";
import { MemberInfoContext } from "../hooks/useMemberInfoContext";

const MemberInfoProvider = ({ children }) => {
  const [memberInfo, setMemberInfo] = useState(null);
  const { token } = useTokenContext();

  const handleFetchError = (error) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.FETCHING_MEMBER_INFO);
  };

  const initMemberInfo = async () => {
    try {
      const { data } = await Api.fetchMemberInfo({ token });

      setMemberInfo(data);
    } catch (error) {
      handleFetchError(error);
    }
  };

  const updateMemberInfo = async (payload) => {
    await Api.fetchMemberInfoEdit({ token, ...payload });
    setMemberInfo((prev) => ({ ...prev, ...payload }));
  };

  useEffect(() => {
    initMemberInfo();
  }, [token]);

  return (
    <MemberInfoContext.Provider value={{ memberInfo, updateMemberInfo }}>
      {children}
    </MemberInfoContext.Provider>
  );
};

export default MemberInfoProvider;
