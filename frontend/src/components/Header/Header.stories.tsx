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
            pathname: "/'",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = () => <Header />;

export const Default = Template.bind({});
