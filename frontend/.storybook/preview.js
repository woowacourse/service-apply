import { addDecorator } from "@storybook/react";
import axios from "axios";
import { initializeWorker, mswDecorator } from "msw-storybook-addon";
import "../src/api/api";
import "../src/App.css";
import { RecruitmentContext } from "../src/hooks/useRecruitmentContext";
import { UserInfoContext } from "../src/hooks/useUserInfoContext";
import { recruitmentDummy, userInfoDummy } from "../src/mock/dummy";
import ModalProvider from "../src/provider/ModalProvider";
import { recruitmentFilter } from "../src/provider/RecruitmentProvider";
import TokenProvider from "../src/provider/TokenProvider";

export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
axios.defaults.baseURL = API_BASE_URL;

initializeWorker();
addDecorator(mswDecorator);

export const parameters = {
  actions: { argTypesRegex: "^on[A-Z].*" },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

export const decorators = [
  (Story) => (
    <RecruitmentContext.Provider
      value={{
        recruitment: recruitmentFilter(recruitmentDummy),
      }}
    >
      <TokenProvider>
        <UserInfoContext.Provider value={{ userInfo: userInfoDummy }}>
          <ModalProvider>
            <Story />
            <div id="modal-root" />
          </ModalProvider>
        </UserInfoContext.Provider>
      </TokenProvider>
    </RecruitmentContext.Provider>
  ),
];
