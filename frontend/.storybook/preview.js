import { MemoryRouter } from "react-router-dom";
import { addDecorator } from "@storybook/react";
import { initializeWorker, mswDecorator } from "msw-storybook-addon";
import axios from "axios";
import "../src/api/api";
import { RecruitmentContext } from "../src/hooks/useRecruitmentContext";
import { recruitmentFilter } from "../src/provider/RecruitmentProvider";
import TokenProvider from "../src/provider/TokenProvider";
import "../src/App.css";
import FormProvider from "../src/provider/FormProvider";
import useForm from "../src/hooks/useForm";

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

const recruitmentDummy = [
  {
    id: 1,
    title: "지원할 제목",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-05T10:00:00",
    endDateTime: "2020-11-05T10:00:00",
    status: "ENDED",
  },
  {
    id: 2,
    title: "웹 백엔드 2기",
    recruitable: true,
    hidden: false,
    startDateTime: "2019-10-25T10:00:00",
    endDateTime: "2019-11-05T10:00:00",
    status: "ENDED",
  },
  {
    id: 3,
    title: "웹 프론트엔드 3기",
    recruitable: true,
    hidden: false,
    startDateTime: "2020-10-25T15:00:00",
    endDateTime: "2021-11-30T10:00:00",
    status: "RECRUITING",
  },
];

export const decorators = [
  (Story) => {
    const method = useForm({});

    return (
      <RecruitmentContext.Provider
        value={{
          recruitment: recruitmentFilter(recruitmentDummy),
        }}
      >
        <FormProvider {...method}>
          <TokenProvider>
            <MemoryRouter>
              <Story />
            </MemoryRouter>
          </TokenProvider>
        </FormProvider>
      </RecruitmentContext.Provider>
    );
  },
];
