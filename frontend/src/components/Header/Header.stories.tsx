import { addDecorator } from "@storybook/react";
import { MemoryRouter } from "react-router-dom";
import Header from "./Header";

type DecoratorFunction = Parameters<typeof addDecorator>[0];

type HeaderMetaData = {
  title: string;
  component: () => JSX.Element;
  decorators?: DecoratorFunction[];
};

const HeaderMeta: HeaderMetaData = {
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

export default HeaderMeta;

const Template = () => <Header />;

export const Default = Template.bind({});
