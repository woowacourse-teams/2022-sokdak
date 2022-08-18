import { useState } from 'react';

import Pagination from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Pagination',
  component: Pagination,
} as ComponentMeta<typeof Pagination>;

const Template: ComponentStory<typeof Pagination> = args => {
  const [currentPage, setCurrentPage] = useState(1);

  return <Pagination {...args} currentPage={currentPage} setCurrentPage={setCurrentPage} />;
};

export const PaginationTemplate = Template.bind({});
PaginationTemplate.args = {
  firstPage: 1,
  lastPage: 11,
};
