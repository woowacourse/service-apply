import axios from "axios";
import { initializeWorker, mswDecorator } from "msw-storybook-addon";
import "../src/api/api";
import "../src/App.css";
import { ModalProvider } from "../src/hooks/useModalContext";
import { RecruitmentContext } from "../src/hooks/useRecruitmentContext";
import { MemberInfoContext } from "../src/hooks/useMemberInfoContext";
import { recruitmentDummy, userInfoDummy } from "../src/mock/dummy";
import { recruitmentFilter } from "../src/provider/RecruitmentProvider";
import TokenProvider from "../src/provider/TokenProvider";

export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
axios.defaults.baseURL = API_BASE_URL;

initializeWorker();

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
        <MemberInfoContext.Provider value={{ userInfo: userInfoDummy }}>
          <ModalProvider>
            <Story />
          </ModalProvider>
        </MemberInfoContext.Provider>
      </TokenProvider>
    </RecruitmentContext.Provider>
  ),
  mswDecorator,
];
