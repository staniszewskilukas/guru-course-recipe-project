package guru.spring.course.recipe.controllers;

import guru.spring.course.recipe.dto.Recipe;
import guru.spring.course.recipe.models.RecipeModel;
import guru.spring.course.recipe.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Łukasz Staniszewski on 2020-02-26
 * @project recipe
 */
class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    RecipeController recipeController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
    }

    @Test
    public void testGetRecipe() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/show/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/show"));
    }

    @Test
    public void testGetNewRecipeForm() throws Exception{
        RecipeModel model = new RecipeModel();

        mockMvc.perform(get("recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeForm"));

    }

    @Test
    public void testSaveRecipe() throws Exception{
        RecipeModel model = new RecipeModel();
        model.setId(3L);
        model.setDescription("TestowyOpis");

        when(recipeService.saveRecipeModel(any())).thenReturn(model);

        mockMvc.perform(post("recipe")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("id","")
        .param("description","someString"))
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:/recipe/show/3"));
    }
}