import { ComponentMeta, ComponentStory } from "@storybook/react";
import SummaryCheckField from "./SummaryCheckField";

export default {
  title: "form/SummaryCheckField",
  component: SummaryCheckField,
} as ComponentMeta<typeof SummaryCheckField>;

const Template: ComponentStory<typeof SummaryCheckField> = (args) => (
  <SummaryCheckField {...args} />
);

export const Default = Template.bind({});
Default.args = {
  label: "약관 동의",
  checked: false,
  children: (
    <p>
      Ullamco duis cillum adipisicing elit Lorem. Culpa veniam aliqua commodo esse culpa officia qui
      duis do consectetur pariatur elit eiusmod. Excepteur nostrud qui quis veniam id consectetur ad
      Lorem cupidatat eu nostrud. Sit tempor sint Lorem Lorem pariatur exercitation non pariatur
      quis.
    </p>
  ),
};
