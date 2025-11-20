import { ComponentMeta, ComponentStory, StoryFn } from "@storybook/react";
import { MemoryRouter } from "react-router-dom";
import LanguageSwitcher from "./LanguageSwitcher";

export default {
  title: "components/LanguageSwitcher",
  component: LanguageSwitcher,
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
} as ComponentMeta<typeof LanguageSwitcher>;

const Template: ComponentStory<typeof LanguageSwitcher> = () => <LanguageSwitcher />;

export const Default = Template.bind({});
