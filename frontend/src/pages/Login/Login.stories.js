import { MemoryRouter } from "react-router-dom";
import Login from "./Login";

export default {
  title: "pages/Login",
  component: Login,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/login",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <Login {...args} />;

export const Default = Template.bind({});

Default.args = {};
