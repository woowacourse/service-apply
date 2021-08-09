import { MemoryRouter } from "react-router-dom";

import RecruitmentProvider from "../src/provider/RecruitmentProvider";
import TokenProvider from "../src/provider/TokenProvider";

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
    <RecruitmentProvider>
      <TokenProvider>
        <MemoryRouter>
          <Story />
        </MemoryRouter>
      </TokenProvider>
    </RecruitmentProvider>
  ),
];
