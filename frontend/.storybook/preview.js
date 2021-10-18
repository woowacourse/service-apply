import { addDecorator } from '@storybook/react';
import axios from 'axios';
import { initializeWorker, mswDecorator } from 'msw-storybook-addon';
import { MemoryRouter } from 'react-router-dom';
import '../src/api/api';
import '../src/App.css';
import useForm from '../src/hooks/useForm';
import { RecruitmentContext } from '../src/hooks/useRecruitmentContext';
import { UserInfoContext } from '../src/hooks/useUserInfoContext';
import { recruitmentDummy, userInfoDummy } from '../src/mock/dummy';
import FormProvider from '../src/provider/FormProvider';
import { recruitmentFilter } from '../src/provider/RecruitmentProvider';
import TokenProvider from '../src/provider/TokenProvider';

export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
axios.defaults.baseURL = API_BASE_URL;

initializeWorker();
addDecorator(mswDecorator);

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
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
            <UserInfoContext.Provider value={{ userInfo: userInfoDummy }}>
              <MemoryRouter>
                <Story />
              </MemoryRouter>
            </UserInfoContext.Provider>
          </TokenProvider>
        </FormProvider>
      </RecruitmentContext.Provider>
    );
  },
];
