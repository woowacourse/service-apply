import React, { useEffect, useState } from "react";
import * as Api from "../api";
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
      alert("유저 정보를 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요.");
    }
  };

  const setUserInfo = async (payload) => {
    try {
      await Api.fetchUserInfoEdit({ token, ...payload });
      _setUserInfo((prev) => ({ ...prev, ...payload }));
    } catch (e) {
      alert("유저 정보를 수정하는데 실패했습니다. 잠시 후 다시 시도해주세요.");
    }
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
