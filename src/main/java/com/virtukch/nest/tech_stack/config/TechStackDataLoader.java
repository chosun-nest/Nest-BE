package com.virtukch.nest.tech_stack.config;

import com.virtukch.nest.tech_stack.model.TechStack;
import com.virtukch.nest.tech_stack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 기술 스택 초기 데이터 자동 생성 클래스
 *
 * <p>데이터베이스가 비어있을 때만 자동으로 기술 스택 데이터를 생성합니다.
 * application.yaml의 설정으로 활성화/비활성화 가능합니다.</p>
 *
 * <p>활성화 방법 (application.yaml):
 * <pre>
 * app:
 *   data:
 *     init:
 *       enabled: true  # 기본값: true
 * </pre>
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(1) // Tag 전에 실행
@ConditionalOnProperty(
    prefix = "app.data.init",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true  // 설정이 없으면 기본적으로 활성화
)
public class TechStackDataLoader implements ApplicationRunner {

    private final TechStackRepository techStackRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 이미 기술 스택 데이터가 있으면 건너뛰기
        if (techStackRepository.count() > 0) {
            log.info("✅ 기술 스택 데이터가 이미 존재합니다. 초기화를 건너뜁니다. (총 {}개)", techStackRepository.count());
            return;
        }

        log.info("💻 기술 스택 데이터베이스가 비어있습니다. 초기 데이터를 생성합니다...");

        List<String> stackNames = List.of(
            // Programming Languages (프로그래밍 언어)
            "Java", "Python", "C", "C++", "C#", "Go", "Rust", "Kotlin", "Swift",
            "JavaScript", "TypeScript", "Dart", "Ruby", "PHP", "Scala", "R",
            "Perl", "Lua", "Haskell", "Elixir", "Objective-C",

            // Web Backend Frameworks (웹 백엔드 프레임워크)
            "Spring", "Spring Boot", "Django", "Flask", "FastAPI", "Express",
            "NestJS", "Ruby on Rails", "ASP.NET", "ASP.NET Core", "Laravel",
            "Symfony", "Gin", "Echo", "Fiber",

            // Web Frontend (웹 프론트엔드)
            "React", "Vue.js", "Angular", "Svelte", "Next.js", "Nuxt.js",
            "Gatsby", "Remix", "SolidJS", "Qwik", "Astro",
            "HTML", "CSS", "Sass", "Less", "Tailwind CSS", "Bootstrap",
            "Material-UI", "Ant Design", "Chakra UI", "jQuery",

            // Mobile Development (모바일 개발)
            "Android", "iOS", "Flutter", "React Native", "Ionic", "Xamarin",
            "SwiftUI", "Jetpack Compose", "Kotlin Multiplatform",

            // Desktop Development (데스크톱 개발)
            "Electron", "Qt", "WPF", "JavaFX", "Tkinter", "Tauri",

            // Databases (데이터베이스)
            "MySQL", "PostgreSQL", "MongoDB", "Redis", "MariaDB", "SQLite",
            "OracleDB", "MSSQL", "DynamoDB", "Elasticsearch", "Cassandra",
            "CouchDB", "Neo4j", "InfluxDB", "TimescaleDB", "Supabase",

            // DevOps & Infrastructure (데브옵스 & 인프라)
            "Docker", "Kubernetes", "Nginx", "Apache", "Terraform", "Ansible",
            "Puppet", "Chef", "Git", "GitHub Actions", "GitLab CI", "Jenkins",
            "CircleCI", "Travis CI", "ArgoCD", "Helm", "Vagrant",

            // Cloud Platforms (클라우드 플랫폼)
            "AWS", "GCP", "Azure", "Firebase", "Vercel", "Netlify", "Heroku",
            "DigitalOcean", "Linode", "Oracle Cloud", "IBM Cloud", "Cloudflare",

            // Machine Learning & AI (머신러닝 & AI)
            "TensorFlow", "PyTorch", "Keras", "scikit-learn", "Pandas", "NumPy",
            "OpenCV", "Hugging Face", "LangChain", "YOLO", "Matplotlib", "Seaborn",

            // Big Data (빅데이터)
            "Apache Spark", "Hadoop", "Kafka", "Flink", "Airflow", "Databricks",

            // Message Queue & Event Streaming (메시지 큐 & 이벤트 스트리밍)
            "RabbitMQ", "ActiveMQ", "NATS", "ZeroMQ",

            // API & Communication (API & 통신)
            "REST API", "GraphQL", "gRPC", "WebSocket", "Socket.io", "tRPC",
            "Swagger", "Postman", "Apollo",

            // Testing (테스팅)
            "JUnit", "Mockito", "Jest", "Mocha", "Chai", "Cypress", "Selenium",
            "Playwright", "Pytest", "TestNG", "JMeter", "K6",

            // Game Development (게임 개발)
            "Unity", "Unreal Engine", "Godot", "Cocos2d", "Phaser", "Three.js",
            "Babylon.js", "PlayCanvas",

            // Blockchain (블록체인)
            "Solidity", "Ethereum", "Web3.js", "Hardhat", "Truffle", "Polygon",

            // Version Control & Collaboration (버전 관리 & 협업)
            "GitHub", "GitLab", "Bitbucket", "Jira", "Confluence", "Notion",
            "Slack", "Discord",

            // Monitoring & Logging (모니터링 & 로깅)
            "Prometheus", "Grafana", "ELK Stack", "Datadog", "New Relic",
            "Sentry", "Logstash", "Kibana", "Splunk",

            // Others (기타)
            "Linux", "Windows", "macOS", "Bash", "PowerShell", "Vim", "VSCode",
            "IntelliJ IDEA", "Eclipse", "Xcode", "Android Studio",
            "Webpack", "Vite", "Rollup", "Babel", "ESLint", "Prettier",
            "OpenGL", "DirectX", "WebGL", "WebAssembly", "FFmpeg"
        );

        List<TechStack> stacks = stackNames.stream()
            .map(name -> TechStack.builder().techStackName(name).build())
            .toList();

        techStackRepository.saveAll(stacks);

        log.info("✅ 기술 스택 {}개 초기화 완료", stacks.size());
        log.info("📊 포함된 카테고리:");
        log.info("  💻 프로그래밍 언어, 웹/모바일 프레임워크");
        log.info("  🗄️ 데이터베이스, 클라우드, DevOps");
        log.info("  🤖 AI/ML, 빅데이터, 블록체인");
        log.info("  🎮 게임 개발, 테스팅, 모니터링 등");
    }
}
