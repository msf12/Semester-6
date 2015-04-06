class CreatePosts < ActiveRecord::Migration
  def change
    create_table :posts do |t|
      t.string :text
      t.datetime :time

      t.timestamps null: false
    end
  end
end
