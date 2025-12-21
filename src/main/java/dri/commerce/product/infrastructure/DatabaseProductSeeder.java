package dri.commerce.product.infrastructure;

import java.math.BigDecimal;
import java.util.List;

import org.jboss.logging.Logger;

import dri.commerce.product.application.usecase.CreateProductUseCase;
import dri.commerce.product.domain.repository.ProductRepository;
import dri.commerce.user.domain.entity.UserDomain;
import dri.commerce.user.domain.enums.Role;
import dri.commerce.user.domain.repository.UserRepository;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.annotation.Priority;

@ApplicationScoped
@IfBuildProfile("dev")
public class DatabaseProductSeeder {

    @Inject
    CreateProductUseCase createProductUseCase;

    @Inject
    ProductRepository productRepository;

    @Inject
    UserRepository userRepository;

    private static final Logger LOG = Logger.getLogger(DatabaseProductSeeder.class);

    // Priority maior que o DatabaseAdminSeeder para garantir que rode depois
    void seedProducts(@Observes @Priority(100) StartupEvent event) {
        LOG.info("DatabaseProductSeeder: verificando se precisa popular produtos...");
        
        // Verifica se já existem produtos
        if (productRepository.count() > 0) {
            LOG.infof("Já existem %d produtos no banco. Pulando seed.", productRepository.count());
            return;
        }

        // Busca um admin ou seller para ser o vendedor dos produtos
        List<UserDomain> admins = userRepository.findByRole(Role.ADMIN);
        if (admins.isEmpty()) {
            admins = userRepository.findByRole(Role.SELLER);
        }
        
        if (admins.isEmpty()) {
            LOG.warn("Nenhum admin/seller encontrado. Pulando seed de produtos.");
            return;
        }

        String sellerId = admins.get(0).id().value();
        LOG.infof("Usando seller_id: %s para criar produtos de seed", sellerId);

        int count = 0;
        
        // Eletrônicos (category_id = 1)
        count += createProducts(sellerId, 1L, List.of(
            new ProductData("iPhone 15 Pro Max 256GB", "8999.00", "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400", 15),
            new ProductData("MacBook Air M3 15\"", "12499.00", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", 8),
            new ProductData("Samsung Galaxy S24 Ultra", "7499.00", "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400", 20),
            new ProductData("AirPods Pro 2ª Geração", "1899.00", "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=400", 50),
            new ProductData("iPad Pro 12.9\" M2", "10999.00", "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400", 12),
            new ProductData("Apple Watch Series 9", "3999.00", "https://images.unsplash.com/photo-1434493789847-2f02dc6ca35d?w=400", 25),
            new ProductData("Sony WH-1000XM5", "2299.00", "https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=400", 30),
            new ProductData("Dell XPS 15 i7 32GB", "14999.00", "https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400", 6),
            new ProductData("Nintendo Switch OLED", "2499.00", "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=400", 18),
            new ProductData("JBL Flip 6 Bluetooth", "699.00", "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400", 40)
        ));

        // Roupas (category_id = 2)
        count += createProducts(sellerId, 2L, List.of(
            new ProductData("Camiseta Básica Algodão Premium", "89.90", "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400", 100),
            new ProductData("Jaqueta Jeans Vintage", "249.90", "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400", 30),
            new ProductData("Vestido Floral Verão", "159.90", "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=400", 45),
            new ProductData("Moletom Oversized Unissex", "179.90", "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=400", 60),
            new ProductData("Calça Jeans Slim Fit", "199.90", "https://images.unsplash.com/photo-1542272454315-4c01d7abdf4a?w=400", 55),
            new ProductData("Blazer Social Masculino", "399.90", "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=400", 20),
            new ProductData("Saia Midi Plissada", "139.90", "https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=400", 35),
            new ProductData("Camisa Social Slim", "159.90", "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=400", 40),
            new ProductData("Shorts Esportivo Dry Fit", "79.90", "https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=400", 80),
            new ProductData("Casaco de Lã Premium", "349.90", "https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=400", 25)
        ));

        // Calçados (category_id = 3)
        count += createProducts(sellerId, 3L, List.of(
            new ProductData("Nike Air Max 90", "799.90", "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", 25),
            new ProductData("Adidas Ultraboost 23", "899.90", "https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400", 18),
            new ProductData("All Star Chuck Taylor", "349.90", "https://images.unsplash.com/photo-1463100099107-aa0980c362e6?w=400", 50),
            new ProductData("Sandália Havaianas Slim", "49.90", "https://images.unsplash.com/photo-1603487742131-4160ec999306?w=400", 100),
            new ProductData("Bota Chelsea Couro", "499.90", "https://images.unsplash.com/photo-1638247025967-b4e38f787b76?w=400", 15),
            new ProductData("Tênis New Balance 574", "599.90", "https://images.unsplash.com/photo-1539185441755-769473a23570?w=400", 22),
            new ProductData("Sapato Social Oxford", "299.90", "https://images.unsplash.com/photo-1614252369475-531eba835eb1?w=400", 30),
            new ProductData("Tênis Vans Old Skool", "449.90", "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=400", 35),
            new ProductData("Mocassim Couro Legítimo", "279.90", "https://images.unsplash.com/photo-1533867617858-e7b97e060509?w=400", 28),
            new ProductData("Chinelo Slide Confort", "89.90", "https://images.unsplash.com/photo-1603487742131-4160ec999306?w=400", 70)
        ));

        // Casa e Decoração (category_id = 4)
        count += createProducts(sellerId, 4L, List.of(
            new ProductData("Luminária de Mesa LED", "189.90", "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=400", 40),
            new ProductData("Sofá 3 Lugares Veludo", "2499.00", "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400", 5),
            new ProductData("Quadro Decorativo Minimalista", "149.90", "https://images.unsplash.com/photo-1513519245088-0e12902e35a6?w=400", 60),
            new ProductData("Tapete Shaggy 2x3m", "399.90", "https://images.unsplash.com/photo-1600166898405-da9535204843?w=400", 15),
            new ProductData("Vaso Cerâmica Artesanal", "79.90", "https://images.unsplash.com/photo-1485955900006-10f4d324d411?w=400", 80),
            new ProductData("Espelho Decorativo Redondo", "249.90", "https://images.unsplash.com/photo-1618220179428-22790b461013?w=400", 25),
            new ProductData("Prateleira Flutuante Kit 3", "129.90", "https://images.unsplash.com/photo-1532372320572-cda25653a26d?w=400", 45),
            new ProductData("Cortina Blackout 3m", "199.90", "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=400", 35),
            new ProductData("Almofada Decorativa 45x45", "59.90", "https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=400", 100),
            new ProductData("Mesa de Centro Industrial", "599.90", "https://images.unsplash.com/photo-1533090161767-e6ffed986c88?w=400", 10)
        ));

        // Esportes e Lazer (category_id = 5)
        count += createProducts(sellerId, 5L, List.of(
            new ProductData("Bicicleta Mountain Bike Aro 29", "1899.00", "https://images.unsplash.com/photo-1532298229144-0ec0c57515c7?w=400", 10),
            new ProductData("Kit Halteres 20kg", "299.90", "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400", 35),
            new ProductData("Esteira Elétrica Dobrável", "2499.00", "https://images.unsplash.com/photo-1576678927484-cc907957088c?w=400", 8),
            new ProductData("Bola de Futebol Oficial", "199.90", "https://images.unsplash.com/photo-1614632537190-23e4b8e63a05?w=400", 50),
            new ProductData("Raquete de Tênis Profissional", "899.90", "https://images.unsplash.com/photo-1617083934551-ac1f1c380a22?w=400", 15),
            new ProductData("Bola de Basquete Spalding", "249.90", "https://images.unsplash.com/photo-1519861531473-9200262188bf?w=400", 40),
            new ProductData("Tapete Yoga Premium 6mm", "129.90", "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400", 60),
            new ProductData("Barraca Camping 4 Pessoas", "449.90", "https://images.unsplash.com/photo-1504280390367-361c6d9f38f4?w=400", 20),
            new ProductData("Skate Longboard Completo", "399.90", "https://images.unsplash.com/photo-1547447134-cd3f5c716030?w=400", 25),
            new ProductData("Corda de Pular Profissional", "49.90", "https://images.unsplash.com/photo-1434596922112-19c563067271?w=400", 100)
        ));

        // Beleza e Saúde (category_id = 6)
        count += createProducts(sellerId, 6L, List.of(
            new ProductData("Perfume Dior Sauvage 100ml", "599.00", "https://images.unsplash.com/photo-1541643600914-78b084683601?w=400", 20),
            new ProductData("Kit Skincare Completo", "299.90", "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=400", 35),
            new ProductData("Secador de Cabelo Profissional", "349.90", "https://images.unsplash.com/photo-1522338242042-2d1c30d4c4c8?w=400", 25),
            new ProductData("Perfume Chanel N°5 50ml", "799.00", "https://images.unsplash.com/photo-1588405748880-12d1d2a59f75?w=400", 15),
            new ProductData("Prancha Alisadora Íon", "249.90", "https://images.unsplash.com/photo-1527799820374-dcf8d9d4a388?w=400", 30),
            new ProductData("Kit Maquiagem Profissional", "449.90", "https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=400", 18),
            new ProductData("Creme Hidratante Corporal 400ml", "89.90", "https://images.unsplash.com/photo-1608248543803-ba4f8c70ae0b?w=400", 80),
            new ProductData("Escova Elétrica Dental", "199.90", "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=400", 45),
            new ProductData("Óleo Corporal Relaxante", "79.90", "https://images.unsplash.com/photo-1600428877878-1a0fd85beda8?w=400", 55),
            new ProductData("Máscara Facial Vitamina C", "59.90", "https://images.unsplash.com/photo-1596755389378-c31d21fd1273?w=400", 70)
        ));

        // Livros (category_id = 7)
        count += createProducts(sellerId, 7L, List.of(
            new ProductData("Clean Code - Robert C. Martin", "89.90", "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400", 60),
            new ProductData("O Programador Pragmático", "79.90", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400", 45),
            new ProductData("Design Patterns GoF", "119.90", "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400", 30),
            new ProductData("Domain-Driven Design", "149.90", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=400", 25),
            new ProductData("Refactoring - Martin Fowler", "109.90", "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=400", 35),
            new ProductData("1984 - George Orwell", "49.90", "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=400", 80),
            new ProductData("O Senhor dos Anéis Box", "199.90", "https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=400", 40),
            new ProductData("Harry Potter Coleção Completa", "399.90", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400", 20),
            new ProductData("O Pequeno Príncipe", "29.90", "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400", 100),
            new ProductData("A Arte da Guerra", "34.90", "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400", 90)
        ));

        // Brinquedos (category_id = 8)
        count += createProducts(sellerId, 8L, List.of(
            new ProductData("LEGO Star Wars Millennium Falcon", "899.90", "https://images.unsplash.com/photo-1558060370-d644479cb6f7?w=400", 12),
            new ProductData("Boneca Barbie Fashionista", "149.90", "https://images.unsplash.com/photo-1613682988402-51d3b4b6a1b7?w=400", 40),
            new ProductData("Hot Wheels Pista Looping", "199.90", "https://images.unsplash.com/photo-1594736797933-d0501ba2fe65?w=400", 30),
            new ProductData("Jogo Uno Original", "29.90", "https://images.unsplash.com/photo-1606503153255-59d7aa0df5f7?w=400", 100),
            new ProductData("Quebra-Cabeça 1000 Peças", "79.90", "https://images.unsplash.com/photo-1494059980473-813e73ee784b?w=400", 50),
            new ProductData("Nerf Elite Blaster", "249.90", "https://images.unsplash.com/photo-1566576912321-d58ddd7a6088?w=400", 25),
            new ProductData("Monopoly Edição Clássica", "159.90", "https://images.unsplash.com/photo-1611891487122-207579d67d98?w=400", 35),
            new ProductData("Pelúcia Urso Gigante 1m", "199.90", "https://images.unsplash.com/photo-1558679908-541bcf1249ff?w=400", 20),
            new ProductData("Carrinho Controle Remoto 4x4", "299.90", "https://images.unsplash.com/photo-1581235720704-06d3acfcb36f?w=400", 18),
            new ProductData("Xadrez Tabuleiro Madeira", "129.90", "https://images.unsplash.com/photo-1529699211952-734e80c4d42b?w=400", 45)
        ));

        // Alimentos e Bebidas (category_id = 9)
        count += createProducts(sellerId, 9L, List.of(
            new ProductData("Kit Café Especial 1kg", "149.90", "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=400", 80),
            new ProductData("Vinho Chileno Reserva 750ml", "89.90", "https://images.unsplash.com/photo-1510812431401-41d2bd2722f3?w=400", 60),
            new ProductData("Whisky 12 Anos 1L", "249.90", "https://images.unsplash.com/photo-1527281400683-1aae777175f8?w=400", 25),
            new ProductData("Chocolate Belga Premium 500g", "79.90", "https://images.unsplash.com/photo-1549007994-cb92caebd54b?w=400", 70),
            new ProductData("Azeite Extra Virgem 500ml", "59.90", "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400", 90),
            new ProductData("Kit Chás Premium 50 Sachês", "69.90", "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400", 55),
            new ProductData("Mel Orgânico 500g", "49.90", "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=400", 100),
            new ProductData("Cerveja Artesanal Kit 6", "99.90", "https://images.unsplash.com/photo-1535958636474-b021ee887b13?w=400", 40),
            new ProductData("Queijo Parmesão Aged 500g", "89.90", "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=400", 35),
            new ProductData("Castanhas Mix Premium 1kg", "129.90", "https://images.unsplash.com/photo-1599599810769-bcde5a160d32?w=400", 45)
        ));

        // Automotivo (category_id = 10)
        count += createProducts(sellerId, 10L, List.of(
            new ProductData("Aspirador de Pó Automotivo 12V", "129.90", "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 55),
            new ProductData("GPS Navegador 7 Polegadas", "349.90", "https://images.unsplash.com/photo-1502920514313-52581002a659?w=400", 30),
            new ProductData("Câmera de Ré HD", "149.90", "https://images.unsplash.com/photo-1489824904134-891ab64532f1?w=400", 40),
            new ProductData("Kit Ferramentas Automotivas 150pcs", "299.90", "https://images.unsplash.com/photo-1530124566582-a618bc2615dc?w=400", 20),
            new ProductData("Capa de Banco Universal", "199.90", "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=400", 35),
            new ProductData("Compressor de Ar Portátil", "179.90", "https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?w=400", 25),
            new ProductData("Suporte Celular Magnético", "59.90", "https://images.unsplash.com/photo-1544636331-e26879cd4d9b?w=400", 80),
            new ProductData("Carregador Veicular USB-C", "49.90", "https://images.unsplash.com/photo-1493238792000-8113da705763?w=400", 100),
            new ProductData("Tapete Automotivo Borracha", "149.90", "https://images.unsplash.com/photo-1489824904134-891ab64532f1?w=400", 45),
            new ProductData("Lavadora Alta Pressão", "699.90", "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 15)
        ));

        LOG.infof("DatabaseProductSeeder: %d produtos criados com sucesso!", count);
    }

    private int createProducts(String sellerId, Long categoryId, List<ProductData> products) {
        int created = 0;
        for (ProductData p : products) {
            try {
                CreateProductUseCase.CreateProductCommand command = new CreateProductUseCase.CreateProductCommand(
                    p.title,
                    new BigDecimal(p.price),
                    p.thumbnail,
                    p.quantity,
                    "new",
                    categoryId,
                    sellerId
                );
                createProductUseCase.execute(command);
                created++;
            } catch (Exception e) {
                LOG.warnf("Erro ao criar produto '%s': %s", p.title, e.getMessage());
            }
        }
        return created;
    }

    private record ProductData(String title, String price, String thumbnail, int quantity) {}
}
