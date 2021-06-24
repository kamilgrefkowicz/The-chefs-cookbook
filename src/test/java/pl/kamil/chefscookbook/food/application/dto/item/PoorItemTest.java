package pl.kamil.chefscookbook.food.application.dto.item;

import org.junit.jupiter.api.Test;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;

class PoorItemTest {

    @Test
    void canMapItemToPoorItem() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        Item item = new Item("testItem", KILOGRAM, INTERMEDIATE, user);
        item.setId(2L);

        PoorItem toTest = new PoorItem(item);

        assertThat(toTest.getId(), equalTo(2L));
        assertThat(toTest.getName(), equalTo("testItem"));
        assertThat(toTest.getUnit(), equalTo(KILOGRAM));
        assertThat(toTest.getType(), equalTo(INTERMEDIATE));
        assertThat(toTest.getUserEntityId(), equalTo(1L));
    }

}