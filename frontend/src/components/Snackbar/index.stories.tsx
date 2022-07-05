import Snackbar from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Snackbar',
  component: Snackbar,
} as ComponentMeta<typeof Snackbar>;

const Template = (args: any) => <Snackbar {...args} />;

export const SnackbarTemplate: ComponentStory<typeof Snackbar> = Template.bind({});
SnackbarTemplate.args = {};
