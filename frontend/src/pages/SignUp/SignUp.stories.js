import { MemoryRouter, Route } from "react-router-dom";
import SignUp from "./SignUp";

export default {
  title: "pages/SignUp",
  component: SignUp,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/applicants/new",
            state: {
              recruitmentId: 3,
            },
          },
        ]}
      >
        <Route path="/applicants/new">
          <Story />
        </Route>
      </MemoryRouter>
    ),
  ],
};

const Template = () => <SignUp />;

export const Default = Template.bind({});
