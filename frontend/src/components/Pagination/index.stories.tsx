import Pagination from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Pagination',
  component: Pagination,
} as ComponentMeta<typeof Pagination>;

const Template: ComponentStory<typeof Pagination> = () => <Pagination />;

export const PaginationTemplate = Template.bind({});
PaginationTemplate.args = {};
