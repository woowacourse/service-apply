import { StoryFn } from "@storybook/react";
import { MemoryRouter } from "react-router-dom";
import Header from "./Header";

export default {
  title: "components/Header",
  component: Header,
  decorators: [
    (Story: StoryFn) => (
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

const Template = () => <Header />;

export const Default = Template.bind({});
