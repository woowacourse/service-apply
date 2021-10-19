import React, { useEffect, useState } from "react";

import * as Api from "../api";
import { ERROR_MESSAGE } from "../constants/messages";
import useTokenContext from "../hooks/useTokenContext";
import { UserInfoContext } from "../hooks/useUserInfoContext";

const UserInfoProvider = ({ children }) => {
  const [userInfo, setUserInfo] = useState(null);
  const { token } = useTokenContext();

  const handleFetchError = (error) => {
    if (!error) return;

    alert(ERROR_MESSAGE.API.FETCHING_USER_INFO);
  };

  const initUserInfo = async () => {
    try {
      const { data } = await Api.fetchUserInfo({ token });

      setUserInfo(data);
    } catch (e) {
      handleFetchError(e);
    }
  };

  const updateUserInfo = async (payload) => {
    await Api.fetchUserInfoEdit({ token, ...payload });
    setUserInfo((prev) => ({ ...prev, ...payload }));
  };

  useEffect(() => {
    initUserInfo();
  }, [token]);

  return (
    <UserInfoContext.Provider value={{ userInfo, updateUserInfo }}>
      {children}
    </UserInfoContext.Provider>
  );
};

export default UserInfoProvider;
