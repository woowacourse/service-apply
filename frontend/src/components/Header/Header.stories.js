import { MemoryRouter, Route } from "react-router-dom";
import Header from "./Header";

export default {
  title: "components/Header",
  component: Header,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <Header {...args} />;

export const Default = Template.bind({});

Default.args = {};
