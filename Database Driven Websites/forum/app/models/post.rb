class Post < ActiveRecord::Base
	has_one :user
	has_one :thread
end
