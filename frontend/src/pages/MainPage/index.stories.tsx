import MainPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/MainPage',
  component: MainPage,
} as ComponentMeta<typeof MainPage>;

const Template = (args: any) => <MainPage {...args} />;

export const MainPageTemplate: ComponentStory<typeof MainPage> = Template.bind({});
MainPageTemplate.args = {};
