import React, { useEffect, useState } from "react";
import * as Api from "../api";
import { ERROR_MESSAGE } from "../constants/messages";
import useTokenContext from "../hooks/useTokenContext";
import { UserInfoContext } from "../hooks/useUserInfoContext";

const UserInfoProvider = ({ children }) => {
  const [userInfo, _setUserInfo] = useState(null);
  const { token } = useTokenContext();

  const initUserInfo = async () => {
    try {
      const { data } = await Api.fetchUserInfo({ token });

      _setUserInfo(data);
    } catch (e) {
      alert(ERROR_MESSAGE.API.FETCHING_USER_INFO);
    }
  };

  const setUserInfo = async (payload) => {
    await Api.fetchUserInfoEdit({ token, ...payload });
    _setUserInfo((prev) => ({ ...prev, ...payload }));
  };

  useEffect(() => {
    initUserInfo();
  }, [token]);

  return (
    <UserInfoContext.Provider value={{ userInfo, setUserInfo }}>
      {children}
    </UserInfoContext.Provider>
  );
};

export default UserInfoProvider;
