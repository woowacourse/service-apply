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
import { recruitmentDummy } from "../src/mock/dummy";

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
